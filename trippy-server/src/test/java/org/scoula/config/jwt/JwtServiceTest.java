package org.scoula.config.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoula.common.dto.TokenPair;
import org.scoula.common.exception.model.UnAuthorizedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-key-for-jwt-token-generation-must-be-long-enough");
        jwtService.init();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("AccessToken 생성 테스트")
    void createAccessToken_Success() {
        // given
        String userId = "123";

        // when
        String accessToken = jwtService.createAccessToken(userId);

        // then
        assertNotNull(accessToken);
        assertTrue(accessToken.length() > 0);
        assertEquals(userId, jwtService.getUserIdInToken(accessToken));
    }

    @Test
    @DisplayName("RefreshToken 생성 테스트")
    void createRefreshToken_Success() {
        // given
        String userId = "123";

        // when
        String refreshToken = jwtService.createRefreshToken(userId);

        // then
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);
        assertEquals(userId, jwtService.getUserIdInToken(refreshToken));
    }

    @Test
    @DisplayName("유효한 토큰 검증 성공 테스트")
    void verifyToken_ValidToken_Success() {
        // given
        String userId = "123";
        String token = jwtService.createAccessToken(userId);

        // when
        boolean result = jwtService.verifyToken(token);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("잘못된 토큰 검증 실패 테스트")
    void verifyToken_InvalidToken_Fail() {
        // given
        String invalidToken = "invalid.token.here";

        // when
        boolean result = jwtService.verifyToken(invalidToken);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("토큰에서 사용자 ID 추출 테스트")
    void getUserIdInToken_Success() {
        // given
        String userId = "123";
        String token = jwtService.createAccessToken(userId);

        // when
        String extractedUserId = jwtService.getUserIdInToken(token);

        // then
        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("토큰 페어 생성 테스트")
    void generateTokenPair_Success() {
        // given
        String userId = "123";

        // when
        TokenPair tokenPair = jwtService.generateTokenPair(userId);

        // then
        assertNotNull(tokenPair);
        assertNotNull(tokenPair.accessToken());
        assertNotNull(tokenPair.refreshToken());
        assertEquals(userId, jwtService.getUserIdInToken(tokenPair.accessToken()));
        assertEquals(userId, jwtService.getUserIdInToken(tokenPair.refreshToken()));
        
        verify(valueOperations).set(eq(userId), eq(tokenPair.refreshToken()), 
                                   eq(JwtService.REFRESH_TOKEN_EXPIRATION_DAYS), eq(TimeUnit.DAYS));
    }

    @Test
    @DisplayName("RefreshToken 비교 성공 테스트")
    void compareRefreshToken_Match_Success() {
        // given
        String userId = "123";
        String refreshToken = "test-refresh-token";
        when(valueOperations.get(userId)).thenReturn(refreshToken);

        // when
        boolean result = jwtService.compareRefreshToken(userId, refreshToken);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("RefreshToken 비교 실패 테스트 - 다른 토큰")
    void compareRefreshToken_Different_Fail() {
        // given
        String userId = "123";
        String storedToken = "stored-refresh-token";
        String providedToken = "different-refresh-token";
        when(valueOperations.get(userId)).thenReturn(storedToken);

        // when
        boolean result = jwtService.compareRefreshToken(userId, providedToken);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("RefreshToken 비교 실패 테스트 - 저장된 토큰 없음")
    void compareRefreshToken_NoStoredToken_Fail() {
        // given
        String userId = "123";
        String refreshToken = "test-refresh-token";
        when(valueOperations.get(userId)).thenReturn(null);

        // when
        boolean result = jwtService.compareRefreshToken(userId, refreshToken);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("RefreshToken 저장 테스트")
    void saveRefreshToken_Success() {
        // given
        String userId = "123";
        String refreshToken = "test-refresh-token";

        // when
        jwtService.saveRefreshToken(userId, refreshToken);

        // then
        verify(valueOperations).set(userId, refreshToken, JwtService.REFRESH_TOKEN_EXPIRATION_DAYS, TimeUnit.DAYS);
    }

    @Test
    @DisplayName("RefreshToken 삭제 테스트")
    void deleteRefreshToken_Success() {
        // given
        String userId = "123";

        // when
        jwtService.deleteRefreshToken(userId);

        // then
        verify(redisTemplate).delete(userId);
    }

    @Test
    @DisplayName("만료된 토큰 검증 시 UnAuthorizedException 발생 테스트")
    void verifyToken_ExpiredToken_ThrowsUnAuthorizedException() {
        // given
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-key-for-jwt-token-generation-must-be-long-enough");
        // ACCESS_TOKEN_EXPIRATION_MINUTE을 매우 짧게 설정하여 즉시 만료되도록 함
        ReflectionTestUtils.setField(jwtService, "ACCESS_TOKEN_EXPIRATION_MINUTE", -1);
        jwtService.init();
        
        String userId = "123";
        String expiredToken = jwtService.createAccessToken(userId);

        // when & then
        assertThrows(UnAuthorizedException.class, () -> jwtService.verifyToken(expiredToken));
    }
}