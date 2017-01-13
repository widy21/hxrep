package com.hx.med.sys.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CodeForm {

  /**
   * 用户账号名称.
   */
  @NotNull(message = "{userForm.account.null}")
  @Pattern(regexp = "(^[A-Z0-9a-z._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$)|(^\\d{11}$)",
    message = "{userForm.account.illegal}")
  @Length(min = 1, max = 60, message = "{userForm.account.length}")
  private String account;

  /**
   * constructor.
   */
  public CodeForm() {
  }

  /**
   * constructor.
   *
   * @param account 账号名称
   */
  public CodeForm(final String account) {
    this.account = account;
  }

  /**
   * get account.
   *
   * @return java.lang.String
   **/
  public String getAccount() {
    return account;
  }

  /**
   * set account.
   *
   * @param account email or phone
   */
  public void setAccount(final String account) {
    this.account = account;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("account", account)
      .toString();
  }
}
