package me.adelemphii.inviteposts.objects;

public enum Groups {

    BUN_CHOPPA("Bun'choppa", "Adele", "https://discord.com/api/webhooks/950522516307337226/Tn6vUUpN_aOgY3637H7EcpE4BJZSP1psHXKRTeIak0C7dUooRnL4F9sk_5EjIOq6m99G"),
    THE_POG_STOG("The POG STOG", "Pogchamp", "https://discord.com/api/webhooks/950522569109426207/3jAXwfgOc66t7MsWnP54b8zsnMtkothQIyLOgHLaoRDRRk5Me_ygqyAoor5I25WuEkiD"),
    BUBBA("Bubba", "Bubba", "https://discord.com/api/webhooks/950522649107394580/ws7bhowkni5kwIl84NbGompsOpTH2e_SlqalSNidb99QodHUDbAkTl15AYT2pOBki6Zh");

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
