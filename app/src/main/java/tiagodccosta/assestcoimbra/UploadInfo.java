package tiagodccosta.assestcoimbra;

import java.util.Date;

public class UploadInfo {

    public String descricao;
    public String url;
    public String key;
    private long postTime;


    public UploadInfo() {

    }

    public UploadInfo(String descricao, String url, String key) {
        this.descricao = descricao;
        this.url = url;
        this.key = key;

        postTime = new Date().getTime();
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

}
