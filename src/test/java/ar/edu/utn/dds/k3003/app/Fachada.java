package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

public class Fachada implements FachadaAgregador {


  @Override
  public FuenteDTO agregar(FuenteDTO fuente) {
    return null;
  }

  @Override
  public List<FuenteDTO> fuentes() {
    return null;
  }

  @Override
  public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException {
    return null;
  }

  @Override
  public List<HechoDTO> hechos(String coleccionId) throws NoSuchElementException {
    return null;
  }

  @Override
  public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {

  }

  @Override
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId)
      throws InvalidParameterException {

  }
}
