package SystemITR.JosueGuinea2A.Departamentos.Controller;

import SystemITR.JosueGuinea2A.Departamentos.DTO.DepartamentoDTO;
import SystemITR.JosueGuinea2A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea2A.Response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    /**Aqui estamos Inyectando la capa service sobre el controller (siempre debe hacerse)*/
    private final DepartamentosService service;
    public DepartamentoController(DepartamentosService service) {
        this.service = service;
    }

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
            ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(true, "Datos ingresados exitosamente", dto);//Aquí el jason ya viene con el id de la base y los datos que se ingresaron
            return ResponseEntity.ok(respuesta);
        }catch (Exception e){
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar", json); //Pasa cuando el dto no recibe nada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }
}
