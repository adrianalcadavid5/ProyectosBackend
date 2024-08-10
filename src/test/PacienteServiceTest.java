package test;

import dao.impl.PacienteDaoH2;
import model.Domicilio;
import model.Paciente;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.PacienteService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PacienteServiceTest {
    static Logger logger = Logger.getLogger(PacienteServiceTest.class);

    //llamo al pacienteSevice y lo vinculo con el pacienteDaoH2 para usarlo en el test
    PacienteService pacienteService = new PacienteService(new PacienteDaoH2());

    @BeforeAll
    static void cargarTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./clinica;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Testear que un paciente se guarde en la base de datos con su domicilio")
    void caso1(){
        //dado ...... un paciente, este debe de ir con el constructor sin el id
        Paciente paciente = new Paciente("Romero","Luciana", "56655", LocalDate.of(2024,7,16),
                new Domicilio("falsa", 456, "Cipolleti", "Rio Negro"));
        //cuando  .. llamamos a nuestro servicio PacienteService y hago arriba
        Paciente pacienteDesdeDB = pacienteService.guardarPaciente(paciente);
        //entonces   -- probamos si el paciente no tiene id no se guarda en la base de datos
        assertNotNull(pacienteDesdeDB.getId());

    }
    @Test
    @DisplayName("Testear que un paciente pueda ser obtenido cuando se envia un id")
    void caso2(){
       //dado   .. id 1 se que esta guardado en la base datos
        Integer id = 1;
       //cuando
        Paciente paciente = pacienteService.buscarPorId(id);
       //enconces
        assertEquals(id, paciente.getId());
    }
}