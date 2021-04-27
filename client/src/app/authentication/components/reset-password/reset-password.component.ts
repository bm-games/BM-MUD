import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-reset-password',
  template: `
    <mat-icon>lock</mat-icon>
    <h3 class="name">Reset your password</h3>
    <form [formGroup]="form" (ngSubmit)="resetPassword()">
      <div class="form-group">
        <input formControlName="email"
               type="email" class="form-control" name="email" placeholder="Email" required>
      </div>
      <div class="form-group">
        <button class="btn btn-primary btn-block" type="submit" [disabled]="!form.valid">Reset Password!</button>
      </div>
    </form>
    <a routerLink="/auth">
      <p class="title">Back to Login</p>
    </a>

  `,
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {

  readonly form = this.fb.group({
    email: ['',  [Validators.required, Validators.email]]
  });

  constructor(private fb: FormBuilder,
              private auth: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
  }

  resetPassword() {
    this.auth.resetPassword(this.form.value.email)
    this.router.navigateByUrl("/auth")
  }

}
