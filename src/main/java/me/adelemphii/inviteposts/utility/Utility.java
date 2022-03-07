package me.adelemphii.inviteposts.utility;

import me.adelemphii.inviteposts.objects.Author;
import me.adelemphii.inviteposts.objects.Groups;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class Utility {

    public static EmbedBuilder createMainEmbed(String title, Color color, Author author, String content, @Nullable File image,
                                       long timestamp, String date, String location, String url) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle(title);
        builder.setUrl(url);
        builder.setColor(color);
        builder.setAuthor(author.getName(), author.getUrl(), author.getImage());
        builder.addField("Details", content);
        builder.addInlineField("Date", date);
        builder.addInlineField("Location", location);
        if(image != null) {
            builder.setImage(image);
        }
        System.out.println(timestamp);
        builder.setTimestamp(Instant.ofEpochMilli(timestamp));

        return builder;
    }

    public static ArrayList<Groups> parseInvitations(String content) {

        ArrayList<Groups> groupsList = new ArrayList<>();

        content = content.replace("@", "");

        for(Groups group : Groups.values()) {
            if(content.contains(group.getName() + " - " + group.getLeader())) {
                groupsList.add(group);
            }
        }
        return groupsList;
    }

    public static ArrayList<Object> scrapeInformation(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            String title = document.title().split("-")[0];

            Elements elements = document.getElementsByClass("ipsType_normal ipsType_richText ipsPadding_bottom ipsContained");

            Element timeElement = document.getElementsByAttribute("datetime").get(0);
            String postTime = timeElement.attr("title");

            DateTime posTime = DateTimeFormat.forPattern("MM/dd/yy hh:mm  a").withZone(DateTimeZone.UTC).parseDateTime(postTime);

            Elements authorElements = document.getElementsByClass("ipsComment_author cAuthorPane ipsColumn ipsColumn_medium ipsResponsive_hidePhone");
            Element authorInfo = authorElements.get(0);
            Element linkElement = authorInfo.getElementsByAttribute("href").get(0);
            Element imageElement = authorInfo.getElementsByAttribute("src").get(0);

            String authorName = authorInfo.text().split(" ")[0];
            String authorLink = linkElement.attr("abs:href");
            String authorImageURL = imageElement.attr("abs:src");
            Author author = new Author(authorName, authorImageURL, authorLink);

            String content = elements.get(0).text();

            String ooc = content.substring(content.indexOf("((") + 2, content.indexOf("))"));
            ooc = ooc.substring(ooc.indexOf("OOC:") + 5);

            String[] split = ooc.split(", ");

            String date = split[0] + ":" + split[1].substring(split[1].indexOf("Time: ") + 6);
            String location = split[2].substring(split[2].indexOf("Location: ") + 10);

            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy:hh:mm a");
            DateTime dateTime = formatter.parseDateTime(date);

            String millis = String.valueOf(dateTime.getMillis());
            millis = millis.substring(0, millis.length() - 3);

            String dateTimestamp = "<t:" + millis + ":f>";

            ArrayList<Groups> invitations = Utility.parseInvitations(content);

            String finalContent = content.substring(content.indexOf("«") + 2, content.indexOf("»"));

            EmbedBuilder builder = Utility.createMainEmbed(title, Color.PINK, author, finalContent, null, posTime.getMillis(),
                    dateTimestamp, location, url);

            ArrayList<Object> objectsToReturn = new ArrayList<>();

            objectsToReturn.add(builder);
            objectsToReturn.add(invitations);
            return objectsToReturn;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
