package SystemITR.JosueGuinea2A.Departamentos.Repository;

import SystemITR.JosueGuinea2A.Departamentos.Entity.DepartamentosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //JPA hace la búsque e hibernate la traducción para el gestor de base de datos
public interface DepartamentosRepository extends JpaRepository<DepartamentosEntity, Long> {
    //Métodos propios

    //Este metodo es personalizado para buscar departamentos por medio de la abreviaruta
    //findBy(atributo del entity)
    Optional<DepartamentosEntity> findByAbreviatura(String abreviatura);
}
