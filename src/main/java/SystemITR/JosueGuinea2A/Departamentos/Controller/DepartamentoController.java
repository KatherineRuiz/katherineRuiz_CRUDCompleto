package SystemITR.JosueGuinea2A.Departamentos.Controller;

import SystemITR.JosueGuinea2A.Departamentos.DTO.DepartamentoDTO;
import SystemITR.JosueGuinea2A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea2A.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/departamentos")
@CrossOrigin //Para abrir la puerta de la api y que acepte peticiones de cualquier origen (solo se usa por el momento ya que en realidad la api no debería aceptar peticiones de cualquier sistema)
public class DepartamentoController {

    /**Aqui estamos Inyectando la capa service sobre el controller (siempre debe hacerse)*/
    private final DepartamentosService service;
    public DepartamentoController(DepartamentosService service) {
        this.service = service;
    }

    /**Todo endPoint tendra:
     * 1. Metodo
     * try
     * 2.Proceso que busca realizar el método
     * 2.1 Generar un log del proceso
     * 3. Creación de una respuesta
     * 4. Retorno
     * catch
     * 1. Creacion de una respuesta
     * 1.1 Generar un log del proceso
     * 2. Retorno
     * */

    /**
     * Nuevo recursos : Ingresar información -> POST
     * Obtener recursos: GET
     * Actualizar recursos: PUT / PATCH
     * Eliminar recursos: DELETE
     */

    //Los <> son para indicar que tipo de dato exacto va a procesar
    @PostMapping
    public ResponseEntity<ApiResponse<DepartamentoDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentoDTO json){ //Este json viene sin id, solo con los datos
        try{
            DepartamentoDTO dto = service.nuevoDepartamento(json);//nuevoDepartamento me devuelve un DepartamentoDTO que es la respuesta de la base
            if (dto != null){
                log.info("Nuevo departamento registrado registrado: " + dto);
                ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(true, "Datos ingresados exitosamente", dto);
                return ResponseEntity.ok(respuesta);
            }
            log.warn("Intento de insersión fallido: " + json);
            ApiResponse<DepartamentoDTO> respuestaFallida = new ApiResponse<>(false, "Intento fallido de insersión", null);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaFallida);
        }catch (Exception e){
            log.error("El proceso presentó fallas inesperadas. Consulte con el administrador");
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar", json); //Pasa cuando el dto no recibe nada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public  ResponseEntity<ApiResponse<List<DepartamentoDTO>>> obtenerDatos(){
        try{
            List<DepartamentoDTO> lista = service.obtenerTodo();
            if (lista != null){
                log.info("Datos de departamentos consultados");
                ApiResponse<List<DepartamentoDTO>> respuestaExito = new ApiResponse<>(true, "Datos encontrados", lista);
                return ResponseEntity.ok(respuestaExito);
            }
            log.info("Datos no encontrados");
            ApiResponse<List<DepartamentoDTO>> respuestaNoEncontrada = new ApiResponse<>(false, "Datos no encontrados", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaNoEncontrada);
        }catch (Exception e){
            log.error("El proceso presentó un fallo inesperado, consulta con el administrador.");
            e.printStackTrace();
            ApiResponse<List<DepartamentoDTO>> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/{id}") //Aquí no se debe poner lista ya que solo busca 1 registro
    public ResponseEntity<ApiResponse<DepartamentoDTO>> obtenerDatosPorId(@PathVariable Long id){
        //PathVariable captura el valor que viene en la URL y la guarda en la variable
        try {
            DepartamentoDTO dto = service.buscarDepartamento(id);
            if (dto != null){
                log.info("Se obtuvieron los datos del departamento con ID: " + id); //Dejar evidencia en el servidor
                ApiResponse<DepartamentoDTO> respuestaExito = new ApiResponse<>(true, "Se obtuvieron los datos el departamento con ID: " + id, dto);
                return ResponseEntity.ok(respuestaExito);
            }
            log.info("Datos no encontrados con ID: " + id);
            ApiResponse<DepartamentoDTO> respuestaNoEncontrada = new ApiResponse<>(false, "Datos no encontrados con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
            //Se usa not_found ya que el proceso que se hizo no trajo nada, no_content, se usa cuando es un status 200(exito), pero no se encontraron datos
        }catch (Exception e){
            log.error("Error crítico al obtener el departamento con ID: " + id);
            e.printStackTrace(); //Se usa para manejo de errore, muestra la ruta de un fallo en el codigo0
            //Ya que el proceso no tuvo una respuesta y no hay datos, usamos el contructor de 2 parametros
            ApiResponse<DepartamentoDTO> respuestaError = new ApiResponse<>(false, "Error crítico al obtener la data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eilimarDepartamento(@PathVariable Long id){
        //Se le pone Void(es un objeto) para decirle al ApiResponse que no retornaremos nada, si se retorna porque es necesarrio, pero la respuesta no devuelve nada
        try {
            boolean respuesta = service.eliminarData(id);
            if (respuesta){
                log.info("Departamento con ID: " + id+ " eliminado");
                ApiResponse<Void> respuestaExitosa = new ApiResponse<>(true, "Departamento con ID: " + id+ "eliminado");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaExitosa);
            }
            log.info("Departamento con ID: " + id+ ", no fue encontrado");
            ApiResponse<Void> respuestaNoEncontrada = new ApiResponse<>(false, "Departamento con ID: " + id+ ", no fue encontrado");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
        }catch (Exception e){
            log.error("Error crítico al eliminar el departamento con ID: " + id);
            e.printStackTrace();
            ApiResponse<Void> respuestaError = new ApiResponse<>(false, "Error crítico al eliminar la data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @PutMapping("/{id}") //Valid es para activar las validaciones del DTO
    public ResponseEntity<ApiResponse<DepartamentoDTO>> actualizarData(@PathVariable Long id, @Valid @RequestBody DepartamentoDTO dto){
        //PUT necesita el id del registro(se captura con el Path) y el requestMapping  captura los nuevos datos
        try {
            DepartamentoDTO data = service.actualizar(id, dto);
            if (data != null){
                log.info("Departamento con ID: "+ id + " ha sido actualizado.");
                ApiResponse<DepartamentoDTO> respuestaExitosa = new ApiResponse<>(true, "Departamento con ID: " + id + " ha sido actualizado.", data);
                return ResponseEntity.ok(respuestaExitosa);
            }
            log.warn("No se pudo completar la actualización del departamento con ID: "+ id);
            ApiResponse<DepartamentoDTO> respuestaNoCompletada = new ApiResponse<>(false, "No se pudo completar la actualización del departamento con ID: "+ id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaNoCompletada);
        }catch (Exception e){
            log.error("Error crítico al actualizar el departamento con ID: " + id);
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuestaError = new ApiResponse<>(false, "Error crítico al actualizar el departamento con ID: " + id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    //Proceso de busqueda por abreviatura
    @GetMapping("/abreviatura/{abreviatura}")
    public ResponseEntity<ApiResponse<DepartamentoDTO>> buscarPorAbreviatura(@PathVariable String abreviatura){
        try {
            DepartamentoDTO data = service.buscarAbreviatura(abreviatura);
            if (data != null){
                log.info("Departamento encontrado con abreviatura: " + abreviatura);
            }
            log.warn("Datos no encontrados con abreviatura: " + abreviatura);
            ApiResponse<DepartamentoDTO> respuestaNoEncontrada = new ApiResponse<>(false, "Datos no encontrados con abreviatura: " + abreviatura);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
        }catch (Exception e){
            log.error("Error crítico al obtener el departamento con abreviatura: " + abreviatura);
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuestaError = new ApiResponse<>(false, "Error crítico al obtener el departamento con abreviatura: " + abreviatura);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}
