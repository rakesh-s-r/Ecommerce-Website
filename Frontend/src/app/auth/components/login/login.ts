import { Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrl: './login.scss',
  standalone: false,
})

export class Login {
  private readonly fb = inject(NonNullableFormBuilder);
  readonly hidePassword = signal(true);

  private authService: any = inject(AuthService);

  readonly loginForm = this.fb.group({
    email: '',
    password: '',
  });

  onSubmit() {
    this.authService.login(this.loginForm.value).subscribe({
      next: (res: any) => console.log(res),
      error: (err: any) => console.log(err),
    });
  }
}
