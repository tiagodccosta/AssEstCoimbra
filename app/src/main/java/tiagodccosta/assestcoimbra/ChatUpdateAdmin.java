package tiagodccosta.assestcoimbra;

import java.util.Date;

public class ChatUpdateAdmin {

    private String adminAns;
    private String id;


    public ChatUpdateAdmin(String adminAns, String id) {
        this.adminAns = adminAns;
        this.id = id;

    }

    public ChatUpdateAdmin() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminAns() {
        return adminAns;
    }

    public void setAdminAns(String adminAns) {
        this.adminAns = adminAns;
    }


}
