import java.time.LocalDate;

public class Produto {
    private int codigo;
    private String descricao;
    private String codigoBarras;
    private String unidade;
    private double precoCompra;
    private double lucro;
    private double precoVenda;
    private Categoria categoria;
    private SubCategoria subCategoria;
    private Fornecedor fornecedor;
    private int garantia ; //em meses
    private String marca;
    private String referencia;
    private LocalDate validade;
    private double comissao;
    private String localizacao;
    private int estoqueAtual;

    public Produto(String codigo, int estoqueAtual, String localizacao, double comissao, LocalDate validade, String marca, double precoCompra, String descricao, Fornecedor fornecedor, double precoVenda, String unidade, String codigoBarras, double lucro, SubCategoria subCategoria, Categoria categoria, int garantia, String referencia) {
        this.codigo = codigo;
        this.estoqueAtual = estoqueAtual;
        this.localizacao = localizacao;
        this.comissao = comissao;
        this.validade = validade;
        this.marca = marca;
        this.precoCompra = precoCompra;
        this.descricao = descricao;
        this.fornecedor = fornecedor;
        this.precoVenda = precoVenda;
        this.unidade = unidade;
        this.codigoBarras = codigoBarras;
        this.lucro = lucro;
        this.subCategoria = subCategoria;
        this.categoria = categoria;
        this.garantia = garantia;
        this.referencia = referencia;
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public double getLucro() {
        return lucro;
    }

    public void setLucro(double lucro) {
        this.lucro = lucro;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public SubCategoria getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(SubCategoria subCategoria) {
        this.subCategoria = subCategoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getGarantia() {
        return garantia;
    }

    public void setGarantia(int garantia) {
        this.garantia = garantia;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }
}
