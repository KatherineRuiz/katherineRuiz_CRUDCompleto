package SystemITR.JosueGuinea2A.Departamentos.Service;

import SystemITR.JosueGuinea2A.Departamentos.DTO.DepartamentoDTO;
import SystemITR.JosueGuinea2A.Departamentos.Entity.DepartamentosEntity;
import SystemITR.JosueGuinea2A.Departamentos.Repository.DepartamentosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartamentosService {

    //Forma 1 de inyección de dependencias y la mas recomendada
    private final DepartamentosRepository repo;
    public DepartamentosService(DepartamentosRepository repo){
        this.repo = repo;
    }

    public DepartamentoDTO nuevoDepartamento(@Valid DepartamentoDTO dto){
        try{
            //1. Convertir a Entity
            DepartamentosEntity entity = convertirAEntity(dto);
            //2. Guardar en la base de datos
            DepartamentosEntity entitySave = repo.save(entity);
            //3. Devolver una respuesta
            return convertirADTO(entitySave);
        }catch (Exception e){
            log.error("Error al ingresar la información del departamento" + e.getMessage());
            return null;
        }
    }

    private DepartamentosEntity convertirAEntity(@Valid DepartamentoDTO dto) {
        DepartamentosEntity objEntity = new DepartamentosEntity();
        objEntity.setNombreDepto(dto.getNombreDepto());
        objEntity.setAbreviatura(dto.getAbreviatura());
        objEntity.setUbicacion(dto.getUbicacion());
        return objEntity;
    }

    private DepartamentoDTO convertirADTO(@Valid DepartamentosEntity entity){
        DepartamentoDTO objDTO = new DepartamentoDTO();
        objDTO.setId(entity.getId()); //El id si debe convertirse a DTO
        objDTO.setNombreDepto(entity.getNombreDepto());
        objDTO.setAbreviatura(entity.getAbreviatura());
        objDTO.setUbicacion(entity.getUbicacion());
        return objDTO;
    }

    public List<DepartamentoDTO> obtenerTodo() {
        //Aquí el retorno es una lista de DepartamentoDTO, pero lo que se recivbe el un entity ya que viene de la base,
        //asi que hay que convertirlo a DTO
        List<DepartamentosEntity> data = repo.findAll();
        return data.stream().map(this::convertirADTO).collect(Collectors.toList());
        //Aqui el .map le da los atributos que vienen del entity, a los atributos del DTo, ej. id(entity) = id(dto)
        //Por eso es necesari que se llamen igual
    }

    public DepartamentoDTO buscarDepartamento(Long id) {
        //Optional me indica si el dato existe o no(puede ser que exista o puede ser que no)
        Optional<DepartamentosEntity> entidadOpcional = repo.findById(id);
        //Si no se usa opcional se estaría diciendoq ue el dato sí existe, en ese caso, se usa el .orElseThrow  para capturar la excepción en caso de que no exista
        return entidadOpcional.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)){ //Validar si existe
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public DepartamentoDTO actualizar(Long id, @Valid DepartamentoDTO dto) {
        try {
            //1. Buscar si el departamento realmente existe
            Optional<DepartamentosEntity> entidadOpcional = repo.findById(id);
            //2. Verificar si el objeto contiene valores (utilizando if)
            if (entidadOpcional.isPresent()){ //Se usa .isPresent porque se necesita un valor booleano
                //2.1 Creamos un objeto de tipo entidad
                DepartamentosEntity entidad = entidadOpcional.get();
                //2.2 Convertir y asignar los dto´s (nuevos valores) a entidad
                entidad.setNombreDepto(dto.getNombreDepto());
                entidad.setAbreviatura(dto.getAbreviatura());
                entidad.setUbicacion(dto.getUbicacion());
                //2.3 Actualizar los datos en la base de datos
                DepartamentosEntity datosGuardados = repo.save(entidad); //El metodo save, identifica si el dato existe(lo actualiza) y si no existe, no agrega
                //2.4 Retornar la data convertida a DTO de forma previa
                return convertirADTO(datosGuardados);
            }
            //3. Si no trae datos, retornal null
            return null;
        }catch (Exception e){
            log.error("Oops, ocurrió un error al precesar la información");
            return null;

        }
    }

    public DepartamentoDTO buscarAbreviatura(String abreviatura) {
        try {
            Optional<DepartamentosEntity> registro = repo.findByAbreviatura(abreviatura);
            if (registro.isPresent()){
                return convertirADTO(registro.get());
            }
            log.warn("No existe ningún departamento con abreviatura: " + abreviatura);
            return null;
        }catch (Exception e){
            log.error("Ocurrió un error durante el proceso");
            return null;
        }
    }
}
