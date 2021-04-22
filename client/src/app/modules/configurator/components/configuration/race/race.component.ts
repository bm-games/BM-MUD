import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-race',
  templateUrl: './race.component.html',
  styleUrls: ['./race.component.scss']
})
export class RaceComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  sliderValue(value: number) {
    return value;
  }

}
