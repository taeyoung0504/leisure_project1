package com.project.leisure.taeyoung.user;


// 회원가입 입력검증 클래스
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	@Size(min = 6, max = 12)
	@NotEmpty(message = "사용자ID는 필수항목입니다.")
	private String username;

	@NotEmpty(message = "비밀번호는 필수항목입니다.")
	private String password1;

	@NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
	private String password2;

	@NotEmpty(message = "이메일은 필수항목입니다.")
	@Email
	private String email;

	@NotEmpty(message = "이메일 인증번호는 필수항목입니다.")
	private String inputCode; // 이메일 인증번호 필드 추가

	@NotEmpty(message = "우편번호는 필수 입력 값입니다.")
	private String addr1;

	private String addr2;
	@NotEmpty(message = "상세주소는 필수 입력 값입니다.")
	private String addr3;

}