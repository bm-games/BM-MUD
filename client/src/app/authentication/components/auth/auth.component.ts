import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-auth-component',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss', './auth.css', './styles.css']
})
export class AuthComponent implements OnInit {

  title = "BM-MUD: Authentication";

  constructor(private titleService: Title) { }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  public setTitle(newTitle: string){
    this.titleService.setTitle(newTitle);
  }

}
