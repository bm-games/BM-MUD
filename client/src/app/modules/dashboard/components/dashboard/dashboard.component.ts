import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  title = "BM-MUD: Dashboard";

  constructor(private titleService: Title) { }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  public setTitle(newTitle: string){
    this.titleService.setTitle(newTitle);
  }

}
