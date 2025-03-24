package io.syebookstore.api.account;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AccountStatusConverter implements AttributeConverter<AccountStatus, String> {

  @Override
  public String convertToDatabaseColumn(AccountStatus status) {
    if (status == null) {
      return null;
    }
    return status.name();
  }

  @Override
  public AccountStatus convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    return AccountStatus.valueOf(dbData);
  }
}
