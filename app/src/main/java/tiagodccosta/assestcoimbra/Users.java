package tiagodccosta.assestcoimbra;

public class Users {

    public String userName;
    public String userCurso;
    public String userAno;
    private String id;

    public Users() {

    }


    public Users(String userName, String userCurso, String userAno, String id) {
        this.userCurso = userCurso;
        this.userName = userName;
        this.userAno = userAno;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserCurso() {
        return userCurso;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCurso(String userCurso) {
        this.userCurso = userCurso;
    }

    public String getUserAno() {
        return userAno;
    }

    public void setUserAno(String userAno) {
        this.userAno = userAno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
