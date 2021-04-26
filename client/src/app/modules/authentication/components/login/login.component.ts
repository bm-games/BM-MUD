import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  template: `
    <mat-icon>lock</mat-icon>
    <h3 class="name">Login</h3>
    <a routerLink="register">
      <p class="title">No Account? Register NOW!</p>
    </a>
    <form [formGroup]="form" (ngSubmit)="login()">
      <div class="form-group">
        <input formControlName="email"
               type="email" class="form-control" name="email" placeholder="Email" required>
      </div>
      <div class="form-group">
        <input formControlName="password"
               type="password" class="form-control" name="password" placeholder="Password" required>
      </div>
      <div class="form-group">
        <button class="btn btn-primary btn-block" type="submit" [disabled]="!form.valid">Log In</button>
      </div>
    </form>
    <a routerLink="reset-password">
      <p class="title">Forgot your Password?</p>
    </a>
  `,
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  readonly form = this.fb.group({
    email: ['',  [Validators.required, Validators.email]],
    password: ['',  Validators.required],
  });

  constructor(private fb: FormBuilder,
              private auth: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
  }

  login() {
    this.auth.login(this.form.value)
    this.router.navigateByUrl("/dashboard")
  }
}
