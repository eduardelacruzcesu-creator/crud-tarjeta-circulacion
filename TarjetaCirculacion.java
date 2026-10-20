package Graficos;

//Clase: "Tarjeta de circulación"
//Encapsulamiento: atributos privados con getters y setters.

public class TarjetaCirculacion {

    // Atributos
    private int id;
    private String numero;
    private int asientos;
    private String modelo;
    private String placa;

    // Constructor vacío
    public TarjetaCirculacion() {
    }

    // Constructor sin id (para insertar, el id lo genera MySQL)
    public TarjetaCirculacion(String numero, int asientos, String modelo, String placa) {
        this.numero = numero;
        this.asientos = asientos;
        this.modelo = modelo;
        this.placa = placa;
    }

    // Constructor completo (para actualizar o consultar)
    public TarjetaCirculacion(int id, String numero, int asientos, String modelo, String placa) {
        this(numero, asientos, modelo, placa);
        this.id = id;
    }

    // Comportamiento
    public void mostrarDatos() {
        System.out.println("ID: " + id);
        System.out.println("Numero: " + numero);
        System.out.println("Asientos: " + asientos);
        System.out.println("Modelo: " + modelo);
        System.out.println("Placa: " + placa);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public int getAsientos() { return asientos; }
    public void setAsientos(int asientos) { this.asientos = asientos; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
}