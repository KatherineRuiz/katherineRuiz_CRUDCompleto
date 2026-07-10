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
}
