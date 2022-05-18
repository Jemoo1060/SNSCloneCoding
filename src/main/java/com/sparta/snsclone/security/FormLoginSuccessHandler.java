package com.sparta.snsclone.security;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.snsclone.dto.ResponseDto;
import com.sparta.snsclone.security.jwt.JwtTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        ResponseDto responseDto = new ResponseDto(200,null);
        response.setStatus(HttpServletResponse.SC_OK);
        OutputStream out = response.getOutputStream();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, responseDto);
        out.flush();
    }

}
