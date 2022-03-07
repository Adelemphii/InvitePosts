package me.adelemphii.inviteposts.objects;

public enum Groups {

    BUN_CHOPPA("Bun'choppa", "Adele", "webhook"),
    THE_POG_STOG("The POG STOG", "Pogchamp", "webhook"),
    BUBBA("Bubba", "Bubba", "webhook");

    private String name;
    private String leader;
    private String webhook;

    Groups(String name, String leader, String webhook) {
        this.name = name;
        this.leader = leader;
        this.webhook = webhook;
    }

    public String getLeader() {
        return leader;
    }

    public String getName() {
        return name;
    }

    public String getWebhook() {
        return webhook;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
