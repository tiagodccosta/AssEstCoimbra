package tiagodccosta.assestcoimbra;

public class UploadInfo {

    public String descricao;
    public String url;
    public String key;

    public UploadInfo() {

    }

    public UploadInfo(String descricao, String url, String key) {
        this.descricao = descricao;
        this.url = url;
        this.key = key;
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

}
