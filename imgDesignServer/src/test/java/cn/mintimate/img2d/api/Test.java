package cn.mintimate.img2d.api;

import io.jsonwebtoken.*;

import java.util.Date;

public class Test {
    // 一天
    private long time=1000*60*60*24;
    //signature
    private String signature="Hello";
    @org.junit.jupiter.api.Test
    public void jwt(){
        JwtBuilder jwtBuilder=Jwts.builder();
        String jwtToken=jwtBuilder
                //header
                .setHeaderParam("type","JWt")
                .setHeaderParam("alg","Hs256")
                //payload
                .claim("username","Mintimate")
                .claim("role","admin")
                .setSubject("Admin-Test")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                //signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        System.out.println(jwtToken);

    }
    @org.junit.jupiter.api.Test
    public void parse(){
        String token="eyJ0eXBlIjoiSld0IiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VybmFtZSI6Ik1pbnRpbWF0ZSIsInJvbGUiOiJhZG1pbiIsInN1YiI6IkFkbWluLVRlc3QiLCJleHAiOjE2MzQ0NTA4Mzl9.8QMJ_bKa4CJXwPlIHO1zYLa5eOh38Ge8ub16wvC6_tE";
        JwtParser jwtParser=Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims=claimsJws.getBody();
        System.out.println(claims.get("username"));
        System.out.println(claims.getExpiration());
    }
}
