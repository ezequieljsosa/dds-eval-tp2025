package ar.edu.utn.dds.k3003.tests.pdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.tests.TestTP;
import java.time.LocalDateTime;
import java.util.List;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PdITest implements TestTP<FachadaProcesadorPdI> {

  private static final String UN_HECHO_ID = "unHechoId";
  public static final PdIDTO PDI = new PdIDTO("", UN_HECHO_ID, "una info", "bsas",
      LocalDateTime.now(), "1234556", List.of());
  public static final PdIDTO PDI2 = new PdIDTO("", UN_HECHO_ID + "2", "una info", "bsas",
          LocalDateTime.now(), "1234556", List.of());

  private FachadaProcesadorPdI instancia;
  
  @Mock
  FachadaSolicitudes fachadaSolicitudes;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    instancia = this.instance();
    instancia.setFachadaSolicitudes(fachadaSolicitudes);
  }

  @Test
  @DisplayName("Procesar 2 PdIs para el mismo hecho")
  void testProcesarPdI() {

    when(fachadaSolicitudes.estaActivo(UN_HECHO_ID)).thenReturn(true);

    val pdi1 = instancia.procesar(PDI);
    instancia.procesar(PDI);

    assertNotNull(pdi1.id(), "El PdI deberia tener un identificador no nulo");
    assertEquals(pdi1.hechoId(), instancia.buscarPdIPorId(pdi1.id()).hechoId(),
        "No se esta recuperando el PdI correctamente");
    assertEquals(2, instancia.buscarPorHecho(UN_HECHO_ID).size(), "No se estan sumando correctamente los PdIs");

  }

  @Test
  @DisplayName("Procesar 2 PdIs en distintos hechos")
  void testProcesarPdIEnDistintosHechos() {

    when(fachadaSolicitudes.estaActivo(PDI.hechoId())).thenReturn(true);
    when(fachadaSolicitudes.estaActivo(PDI2.hechoId())).thenReturn(true);

    instancia.procesar(PDI);
    instancia.procesar(PDI2);

    assertEquals(1, instancia.buscarPorHecho(UN_HECHO_ID).size(), "No se estan sumando correctamente los PdIs");
    assertEquals(1, instancia.buscarPorHecho(PDI2.hechoId()).size(), "No se estan sumando correctamente los PdIs");

  }

  @Test
  @DisplayName("No reprocesar PdIs")
  void testReProcesarPdI() {

    when(fachadaSolicitudes.estaActivo(UN_HECHO_ID)).thenReturn(true);

    instancia.procesar(PDI);
    instancia.procesar(PDI);

    assertEquals(1, instancia.buscarPorHecho(UN_HECHO_ID).size(),
            "Si se intenta procesar 2 veces el mismo PDI, no se deberia volver a agrega / reprocesar");

  }

  @Test
  @DisplayName("Procesar PdI que fue borrado")
  void testProcesarPdICerrado() {

    when(fachadaSolicitudes.estaActivo(UN_HECHO_ID)).thenReturn(false);

    assertThrows( IllegalStateException.class, () -> instancia.procesar(PDI));

  }

  @Override
  public String paquete() {
    return PAQUETE_BASE + "tests.pdi";
  }

  @Override
  public Class<FachadaProcesadorPdI> clase() {
    return FachadaProcesadorPdI.class;
  }
}
