package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {
  public static final AuthAuthorityEntityRowMapper instance = new AuthAuthorityEntityRowMapper();

  private AuthAuthorityEntityRowMapper() {
  }

  @Override
  public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthUserDao aud = new AuthUserDaoJdbc();
    AuthorityEntity result = new AuthorityEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUser(aud.findById(rs.getObject("user_id", UUID.class)).get());
    result.setAuthority(Authority.valueOf(rs.getString("authority")));
    return result;
  }
}