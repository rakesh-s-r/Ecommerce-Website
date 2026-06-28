import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.scss',
  standalone: false
})
export class Login {
  loginForm: FormGroup;
  hidePassword: boolean = true;

  private authService: any = inject(AuthService)

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: '',
      password: '',
    });
  }

  onSubmit() {
    this.authService.login(this.loginForm.value).subscribe({
      next: (res: any) => console.log(res),
      error: (err: any) => console.log(err)
    })
  }
}
