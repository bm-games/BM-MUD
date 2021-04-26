import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-auth-component',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

  title = "BM-MUD: Authentication";

  constructor(private titleService: Title,
              private auth: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  public setTitle(newTitle: string){
    this.titleService.setTitle(newTitle);
  }

  login() {
    this.auth.login()
      .then(() => this.router.navigateByUrl("/dashboard"));
  }
}
