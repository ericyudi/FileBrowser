package appfilebrowse;

public class Arquivo 
{
    private String nome;
    private long tamanho;
    private boolean pasta;

    public Arquivo(String nome, long tamanho, boolean pasta) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.pasta = pasta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(long tamanho) {
        this.tamanho = tamanho;
    }

    public boolean isPasta() {
        return pasta;
    }

    public void setPasta(boolean pasta) {
        this.pasta = pasta;
    }
    
    
}
