package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	/* Tempo de expiração do TOKEN valido para 2 dias como exemplo */
	private static final long EXPIRATION_TIME = 172800000;

	/* Uma senha unica para compor a autenticação e ajudar na segurança */
	private static final String SECRET = "SenhaExtremamenteSecreta";

	/* Prefixo padrão de TOKEN */
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	/* Gerando TOKEN de autenticação e adicionando ao cabeçalho e resposta Http */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {

		/* Montagem do TOKEN */
		String JWT = Jwts.builder() /*Chama o gerador de Token*/
				.setSubject(username)/*Adiciona o usuário*/
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))/*Tempo de expiraçao*/
				.signWith(SignatureAlgorithm.HS512, SECRET).compact(); /*Compactação e algoritmos de geração de senha*/

		/*Junta Token com o prefixo*/
		String token = TOKEN_PREFIX + " " + JWT; /*Bearer 878787wewewe87878ew87ew87e8w78we8we EXEMPLO*/
	
		/*Adiciona no cabeçalho Http*/
		response.addHeader(HEADER_STRING, token); /*Authorization: Bearer 878787wewewe87878ew87ew87e8w78we8we */

		/*Liberando resposta para portas diferentes que usam a API ou caso clientes Web*/
		liberacaoCors(response);
		
		/* Escreve token como resposta no corpo http */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

	/*Retorna o usuário validado com token ou caso não seja valido retorna null*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		/* Pega o token enviado no cabeçalho http */
		String token = request.getHeader(HEADER_STRING);
	
		try {
		if (token != null) {

			String tokenLimpo = token.replace(TOKEN_PREFIX,"").trim();
			
		/*Faz a validação do token do usuario na requisição*/
		String user = Jwts.parser().setSigningKey(SECRET) /*neste momento esta assim Bearer 878787wewewe87878ew87ew87e8w78we8we */
			              .parseClaimsJws(tokenLimpo) /*Agora esta 878787wewewe87878ew87ew87e8w78we8we*/
			              .getBody().getSubject(); /*depois de tudo finalizado vai retorna por exemplo João Silva*/  
		
		if (user != null) {

			Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
					.findUserByLogin(user);

			if (usuario != null) {
				
				if(tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
				
				return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
						usuario.getAuthorities());
				}
				
				}

		}

	}
} catch (io.jsonwebtoken.ExpiredJwtException e) {
	try {
		response.getOutputStream()
				.print("Seu TOKEN está expirado, faça o login ou informe um novo Token para autenticaçaão ");
	} catch (IOException e1) {

	}
}
		
		liberacaoCors(response);
	return null; /* Nao autorizado */

}

private void liberacaoCors(HttpServletResponse response) {
	if (response.getHeader("Access-Control-Allow-Origin") == null) {
		response.addHeader("Access-Control-Allow-Origin", "*");
	}

	if (response.getHeader("Access-Control-Allow-Headers") == null) {
		response.addHeader("Access-Control-Allow-Headers", "*");
	}

	if (response.getHeader("Access-Control-Request-Headers") == null) {
		response.addHeader("Access-Control-Request-Headers", "*");
	}

	if (response.getHeader("Access-Control-Allow-Methods") == null) {
		response.addHeader("Access-Control-Request-Methods", "*");
	}
}

}

