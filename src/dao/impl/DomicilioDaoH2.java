package dao.impl;

import dao.IDao;
import db.H2Connection;
import model.Domicilio;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class DomicilioDaoH2 implements IDao<Domicilio> {
    public static final Logger logger = Logger.getLogger(DomicilioDaoH2.class);

    public static final String INSERT = "INSERT INTO DOMICILIOS VALUES (DEFAULT,?,?,?,?)";

    public static final String SELECT_ID = "SELECT * FROM DOMICILIOS WHERE ID = ?";

    @Override
    public Domicilio guardar(Domicilio domicilio) {
        //Guardar como todos los metodos crud debemos de crear una transacion por si algo sale mal
        Connection connection = null;
        Domicilio domicilioARetornar = null;

        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, domicilio.getCalle());
            preparedStatement.setInt(2,domicilio.getNumero());
            preparedStatement.setString(3, domicilio.getLocalidad());
            preparedStatement.setString(4, domicilio.getProvincia());
            preparedStatement.executeUpdate();
            connection.commit();

            //traer la clave primaria, primero se crea docicilio por que tienen una relacion de uno  auno
            //y necesita tener creado el domicilio para poder hacer paciente por que este lleva un domicilio
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            //se pone if y no while por que viene 1 sola fila, al buscar por id
            if (resultSet.next()){
                Integer id = resultSet.getInt(1);
                domicilioARetornar = new Domicilio(id,domicilio.getCalle(), domicilio.getNumero(),
                        domicilio.getLocalidad(), domicilio.getProvincia());
            }
            logger.info("domicilio persistido "+ domicilioARetornar);

//utilizamos el catch largo y se puede pegar de otro proyecto
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                    e.printStackTrace();
                }
            }
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return domicilioARetornar;
    }

    @Override
    public Domicilio buscarPorId(Integer id) {
        Connection connection = null;
        Domicilio domicilioEncontrado = null;
        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Integer idDB = resultSet.getInt(1);
                String calle = resultSet.getNString(2);
                int numero = resultSet.getInt(3);
                String localidad = resultSet.getNString(4);
                String provincia = resultSet.getNString(5);
                domicilioEncontrado = new Domicilio(idDB,calle,numero,localidad,provincia);
            }
            if (domicilioEncontrado !=null){
                logger.info("domicilio encontrado " + domicilioEncontrado);
            }else logger.info("domicilio no encontrado");

        }catch (Exception e){    //catch corto, va hasta antes del return
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            }catch (SQLException e){
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return domicilioEncontrado;
    }

    @Override
    public List<Domicilio> buscarTodos() {
        return List.of();
    }
}
