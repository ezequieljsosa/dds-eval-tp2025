package ar.edu.utn.dds.k3003.tests.solicitudes;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.tests.TestTP;
import java.util.NoSuchElementException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SolicitudesTest implements TestTP<FachadaSolicitudes> {

  private static final String HECHO_ID = "123";
  private static final String TITULO_COLECCION = "123";

  FachadaSolicitudes instancia;
  @Mock
  FachadaFuente fachadaFuente;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    instancia = this.instance();
    instancia.setFachadaFuente(fachadaFuente);
  }

  @Test
  @DisplayName("Asignar un traslado a una vianda que no existe")
  void testAgregarSolicitud() {
    when(fachadaFuente.buscarHechoXId(HECHO_ID)).thenReturn(
        new HechoDTO(HECHO_ID, TITULO_COLECCION));
    var solicitudDTO = instancia.agregar(
        new SolicitudDTO("", "una solicitud", EstadoSolicitudBorradoEnum.CREADA, HECHO_ID));

    assertNotNull(solicitudDTO.id(), "La solicitud tendria que tener un identificador");

    verify(fachadaFuente, description("la")).buscarHechoXId(HECHO_ID);
  }

  @Test
  @DisplayName("Asignar un traslado a una vianda que no existe")
  void testAgregarSolicitudNoExisteHecho() {
    when(fachadaFuente.buscarHechoXId(HECHO_ID)).thenThrow(NoSuchElementException.class);

    assertThrows(
        NoSuchElementException.class,
        () -> instancia.agregar(
            new SolicitudDTO("", "una solicitud", EstadoSolicitudBorradoEnum.CREADA, HECHO_ID)),
        "Si el hecho no existe, el agregado de la solicitud deberia fallar");
  }


  @Override
  public String paquete() {
    return PAQUETE_BASE + "tests.solicitudes";
  }

  @Override
  public Class<FachadaSolicitudes> clase() {
    return FachadaSolicitudes.class;
  }
}
