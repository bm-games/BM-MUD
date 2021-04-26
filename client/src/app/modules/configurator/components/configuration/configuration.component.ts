import { Component, OnInit } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {RaceConfig} from "../../models/RaceConfig";

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.scss']
})
export class ConfigurationComponent implements OnInit {

  title = "BM-MUD: Configurator";

  constructor(private titleService: Title) { }

  ngOnInit(): void {
    this.setTitle(this.title);
  }

  public setTitle(newTitle: string){
    this.titleService.setTitle(newTitle);
  }

}
