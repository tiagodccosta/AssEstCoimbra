package tiagodccosta.assestcoimbra;

public class UploadInfo {

    public String descricao;
    public String url;

    public UploadInfo() {

    }

    public UploadInfo(String descricao, String url) {
        this.descricao = descricao;
        this.url = url;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrl() {
        return url;
    }

}
