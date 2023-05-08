package ac.at.fhcampuswien.authenticationms.controller;

import ac.at.fhcampuswien.authenticationms.dto.ExtractedEmailDto;
import ac.at.fhcampuswien.authenticationms.dto.RefreshTokenDTO;
import ac.at.fhcampuswien.authenticationms.dto.RefreshTokenResponseDTO;
import ac.at.fhcampuswien.authenticationms.exceptions.CustomerNotFoundException;
import ac.at.fhcampuswien.authenticationms.exceptions.InvalidTokenException;
import ac.at.fhcampuswien.authenticationms.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for managing authentication")
public class AuthController {

    @Autowired
    JwtService jwtService;
    @PostMapping("/refreshtoken")
    @Operation(
            summary = "Refresh access token.",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenDTO.class))),
                    @ApiResponse(description = "Invalid Token", responseCode = "401", content = @Content)
            })
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) throws InvalidTokenException, CustomerNotFoundException {
        String token = refreshTokenDTO.getRefreshToken();
        String eMail = jwtService.extractUserEmail(token);
        jwtService.isTokenExpiredOrInvalid(token);
        String accessToken = jwtService.generateToken(eMail, JwtService.Token.AccessToken);

        RefreshTokenResponseDTO refreshTokenResponseDTO = new RefreshTokenResponseDTO(accessToken);
        return new ResponseEntity<>(refreshTokenResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/eMail")
    @Operation(
            summary = "Extract Email.",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExtractedEmailDto.class)))
            })
    public ResponseEntity<ExtractedEmailDto> extractEmailFromToken(@RequestParam("token") String token){
        String eMail = jwtService.extractUserEmail(token);
        ExtractedEmailDto extractedEmailDto = new ExtractedEmailDto(eMail);
        return new ResponseEntity<>(extractedEmailDto, HttpStatus.OK);
    }

    @GetMapping("/validation")
    @Operation(
            summary = "validate Token",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Token is invalid", responseCode = "401")
            })
    public void checkTokenValidity(@RequestParam("token") String token) throws CustomerNotFoundException, InvalidTokenException {
        jwtService.isTokenExpiredOrInvalid(token);
    }


}
