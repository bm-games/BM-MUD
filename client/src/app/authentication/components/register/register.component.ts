import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  template: `
    <mat-icon>lock</mat-icon>
    <h3 class="name">Register</h3>
    <a routerLink="/auth">
      <p class="title">Already got an account? Login!</p>
    </a>
    <form [formGroup]="form" (ngSubmit)="register()">
      <div class="form-group">
        <input formControlName="email"
               type="email" class="form-control" name="email" placeholder="Email" required>
      </div>
      <div class="form-group">
        <input formControlName="username"
               type="text" class="form-control" name="username" placeholder="Username">
      </div>
      <div class="form-group">
        <input formControlName="password"
               type="password" class="form-control" name="password" placeholder="Password" required>
      </div>
      <div class="form-group">
        <button class="btn btn-primary btn-block" type="submit" [disabled]="!form.valid">Register Now!</button>
      </div>
    </form>
  `,
  styleUrls: ['./register.component.scss', '../auth/auth.css', '../auth/styles.css'],
})
export class RegisterComponent implements OnInit {

  readonly form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  constructor(private fb: FormBuilder,
              private auth: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
  }

  async register() {
    this.form.disable()
    await this.auth.register(this.form.value)
      .catch(({error}) => alert(error))
      .then(() => {
        alert("Das hat geklappt. Bitte verifiziere deine Email, dann kannst du sofort loslegen!")
        this.router.navigateByUrl("/auth")
      });
    this.form.enable()
  }

}
