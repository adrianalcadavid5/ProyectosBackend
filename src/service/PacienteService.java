package service;

import dao.IDao;
import model.Paciente;

public class PacienteService {
    //tienen una implementacion de pacienteIDao y esto obliga a un constructor
    private IDao<Paciente> pacienteIDao;

    public PacienteService(IDao<Paciente> pacienteIDao) {
        this.pacienteIDao = pacienteIDao;
    }

    //aca implementamos los metodos que trabajos en dao Impl ejemplo PacienteDaoH2
    public Paciente guardarPaciente(Paciente paciente){
        return pacienteIDao.guardar(paciente);
    }
    public Paciente buscarPorId(Integer id){
        return pacienteIDao.buscarPorId(id); // despues vamos al test
    }
}
