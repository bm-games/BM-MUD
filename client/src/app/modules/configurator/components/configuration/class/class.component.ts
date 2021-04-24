import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-class',
  templateUrl: './class.component.html',
  styleUrls: ['./class.component.scss']
})
export class ClassComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  sliderValue(value: number) {
    return value;
  }

}
