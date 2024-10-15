package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

  public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

  private AuthorityEntityRowMapper() {
  }

  @Override
  public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthorityEntity ae = new AuthorityEntity();
    AuthUserDao aud = new AuthUserDaoJdbc();
    ae.setId(rs.getObject("id", UUID.class));
    ae.setUser(aud.findById(rs.getObject("user_id", UUID.class)).get());
    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
    return ae;
  }
}