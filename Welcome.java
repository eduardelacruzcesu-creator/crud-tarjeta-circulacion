package Graficos;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//Clase principal: formulario gráfico del CRUD de Tarjeta de circulación.

public class Welcome extends JFrame {

    // Datos de conexion JDBC
    private static final String URL = "jdbc:mysql://127.0.0.1/tarjetas_db";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    // Menu
    JMenuBar mb;
    JMenu archivo, bd;
    JMenuItem mi_salir, bd_C, bd_R, bd_U, bd_D;

    // Etiquetas y campos
    JLabel lb_titulo, lb_id, lb_numero, lb_asientos, lb_modelo, lb_placa;
    JTextField txt_id, txt_numero, txt_asientos, txt_modelo, txt_placa;

    // Botones
    JButton btn_buscar, btn_consultar, btn_alta, btn_actualizar, btn_eliminar;

    public Welcome() {
        this.setTitle("Tarjeta de circulacion");
        this.setBounds(150, 50, 800, 420);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);
        this.setLayout(null);

        // ---------- Barra de menu ----------
        mb = new JMenuBar();
        this.setJMenuBar(mb);

        archivo = new JMenu("Archivo");
        mb.add(archivo);
        mi_salir = new JMenuItem("Salir");
        mi_salir.addActionListener(e -> System.exit(0));
        archivo.add(mi_salir);

        bd = new JMenu("Base de datos");
        mb.add(bd);

        bd_C = new JMenuItem("C - Crear");
        bd_C.addActionListener(e -> insertar());
        bd.add(bd_C);

        bd_R = new JMenuItem("R - Consultar");
        bd_R.addActionListener(e -> consultar());
        bd.add(bd_R);

        bd_U = new JMenuItem("U - Actualizar");
        bd_U.addActionListener(e -> actualizar());
        bd.add(bd_U);

        bd_D = new JMenuItem("D - Eliminar");
        bd_D.addActionListener(e -> eliminar());
        bd.add(bd_D);

        // ---------- Etiquetas y campos ----------
        lb_titulo = new JLabel("Tarjeta de circulación");
        lb_titulo.setBounds(330, 10, 250, 30);

        lb_id = new JLabel("Id");
        lb_id.setBounds(100, 50, 80, 30);
        txt_id = new JTextField();
        txt_id.setBounds(190, 50, 250, 30);

        lb_numero = new JLabel("Número");
        lb_numero.setBounds(100, 90, 80, 30);
        txt_numero = new JTextField();
        txt_numero.setBounds(190, 90, 250, 30);

        lb_asientos = new JLabel("Asientos");
        lb_asientos.setBounds(100, 130, 80, 30);
        txt_asientos = new JTextField();
        txt_asientos.setBounds(190, 130, 250, 30);

        lb_modelo = new JLabel("Modelo");
        lb_modelo.setBounds(100, 170, 80, 30);
        txt_modelo = new JTextField();
        txt_modelo.setBounds(190, 170, 250, 30);

        lb_placa = new JLabel("Placa");
        lb_placa.setBounds(100, 210, 80, 30);
        txt_placa = new JTextField();
        txt_placa.setBounds(190, 210, 250, 30);

        // ---------- Botones ----------
        btn_buscar = new JButton("ID");
        btn_buscar.setBounds(60, 280, 130, 40);
        btn_buscar.addActionListener(e -> buscarPorId());

        btn_consultar = new JButton("Consultar");
        btn_consultar.setBounds(200, 280, 130, 40);
        btn_consultar.addActionListener(e -> consultar());

        btn_alta = new JButton("Alta");
        btn_alta.setBounds(340, 280, 130, 40);
        btn_alta.addActionListener(e -> insertar());

        btn_actualizar = new JButton("Actualizar");
        btn_actualizar.setBounds(480, 280, 130, 40);
        btn_actualizar.addActionListener(e -> actualizar());

        btn_eliminar = new JButton("Eliminar");
        btn_eliminar.setBounds(620, 280, 130, 40);
        btn_eliminar.addActionListener(e -> eliminar());

        // ---------- Agregar componentes ----------
        this.add(lb_titulo);
        this.add(lb_id);       this.add(txt_id);
        this.add(lb_numero);   this.add(txt_numero);
        this.add(lb_asientos); this.add(txt_asientos);
        this.add(lb_modelo);   this.add(txt_modelo);
        this.add(lb_placa);    this.add(txt_placa);
        this.add(btn_buscar);
        this.add(btn_consultar);
        this.add(btn_alta);
        this.add(btn_actualizar);
        this.add(btn_eliminar);

        this.setVisible(true);
    }

    // ================== VALIDACIONES ==================
    private TarjetaCirculacion leerFormulario(boolean requiereId) {
        String numero = txt_numero.getText().trim();
        String asientosTxt = txt_asientos.getText().trim();
        String modelo = txt_modelo.getText().trim();
        String placa = txt_placa.getText().trim();

        if (numero.isEmpty() || asientosTxt.isEmpty() || modelo.isEmpty() || placa.isEmpty()) {
            mensaje("No se permiten campos vacíos.");
            return null;
        }

        int asientos;
        try {
            asientos = Integer.parseInt(asientosTxt);
            if (asientos <= 0) {
                mensaje("Los asientos deben ser mayores a 0.");
                return null;
            }
        } catch (NumberFormatException ex) {
            mensaje("Los asientos deben ser un número entero.");
            return null;
        }

        TarjetaCirculacion t = new TarjetaCirculacion(numero, asientos, modelo, placa);

        if (requiereId) {
            int id = leerId();
            if (id < 0) {
                return null;
            }
            t.setId(id);
        }
        return t;
    }

    //Lee el id del formulario. Devuelve -1 si no es valido.
    private int leerId() {
        String idTxt = txt_id.getText().trim();
        if (idTxt.isEmpty()) {
            mensaje("Escribe el Id.");
            return -1;
        }
        try {
            return Integer.parseInt(idTxt);
        } catch (NumberFormatException ex) {
            mensaje("El Id debe ser un número entero.");
            return -1;
        }
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    private void limpiarCampos() {
        txt_id.setText("");
        txt_numero.setText("");
        txt_asientos.setText("");
        txt_modelo.setText("");
        txt_placa.setText("");
    }

    // ================== CRUD ==================
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
    
    public void insertar() {
        TarjetaCirculacion t = leerFormulario(false);
        if (t == null) {
            return;
        }
        String sql = "INSERT INTO tarjetas_circulacion (numero, asientos, modelo, placa) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection cnx = conectar(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, t.getNumero());
            ps.setInt(2, t.getAsientos());
            ps.setString(3, t.getModelo());
            ps.setString(4, t.getPlaca());
            ps.executeUpdate();

            mensaje("Registro insertado.");
            limpiarCampos();
        } catch (SQLException ex) {
            mensaje("Error al insertar: " + ex.getMessage());
        }
    }

    public void consultar() {
        String sql = "select * from tarjetas_circulacion";
        try (Connection cnx = conectar();
             PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("--- CONSULTA BD ---");
            while (rs.next()) {
                TarjetaCirculacion t = new TarjetaCirculacion(
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getInt("asientos"),
                        rs.getString("modelo"),
                        rs.getString("placa"));
                System.out.println("ID: " + t.getId()
                        + " | Numero: " + t.getNumero()
                        + " | Asientos: " + t.getAsientos()
                        + " | Modelo: " + t.getModelo()
                        + " | Placa: " + t.getPlaca());
            }
        } catch (SQLException ex) {
            mensaje("Error al consultar: " + ex.getMessage());
        }
    }

    //Buscar por ID: llena el formulario con el registro encontrado 
    public void buscarPorId() {
        int id = leerId();
        if (id < 0) {
            return;
        }
        String sql = "SELECT * FROM tarjetas_circulacion WHERE id = ?";
        try (Connection cnx = conectar(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txt_numero.setText(rs.getString("numero"));
                    txt_asientos.setText(String.valueOf(rs.getInt("asientos")));
                    txt_modelo.setText(rs.getString("modelo"));
                    txt_placa.setText(rs.getString("placa"));
                } else {
                    mensaje("No existe una tarjeta con el Id " + id);
                }
            }
        } catch (SQLException ex) {
            mensaje("Error al buscar: " + ex.getMessage());
        }
    }

    //Actualizar: modifica la tarjeta con el id 
    public void actualizar() {
        TarjetaCirculacion t = leerFormulario(true);
        if (t == null) {
            return;
        }
        String sql = "UPDATE tarjetas_circulacion SET numero = ?, asientos = ?, modelo = ?, "
                + "placa = ? WHERE id = ?";
        try (Connection cnx = conectar(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, t.getNumero());
            ps.setInt(2, t.getAsientos());
            ps.setString(3, t.getModelo());
            ps.setString(4, t.getPlaca());
            ps.setInt(5, t.getId());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                mensaje("Registro actualizado.");
            } else {
                mensaje("No existe una tarjeta con el Id " + t.getId());
            }
        } catch (SQLException ex) {
            mensaje("Error al actualizar: " + ex.getMessage());
        }
    }

    //Eliminar: borra la tarjeta con el id 
    
    public void eliminar() {
        int id = leerId();
        if (id < 0) {
            return;
        }
        String sql = "DELETE FROM tarjetas_circulacion WHERE id = ?";
        try (Connection cnx = conectar(); PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                mensaje("Registro eliminado.");
                limpiarCampos();
            } else {
                mensaje("No existe una tarjeta con el Id " + id);
            }
        } catch (SQLException ex) {
            mensaje("Error al eliminar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Welcome();
    }
}