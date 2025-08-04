package org.scoula.common.exception.model;

import lombok.Getter;
import org.scoula.common.exception.enums.ErrorCode;


public class ServerErrorException extends TrippyException {
  public ServerErrorException(ErrorCode errorCode) {
    super(errorCode);
  }
}
