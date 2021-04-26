import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ConfigurationComponent} from "./components/configuration/configuration.component";
import {RaceComponent} from "./components/configuration/race/race.component";
import {ClassComponent} from "./components/configuration/class/class.component";
import {RoomComponent} from "./components/configuration/room/room.component";
import {NPCComponent} from "./components/configuration/npc/npc.component";
import {StartequipmentComponent} from "./components/configuration/startequipment/startequipment.component";
import {CommandComponent} from "./components/configuration/command/command.component";
import {ItemComponent} from "./components/configuration/item/item.component";

const routes: Routes = [
  {
    path: '',
    component: ConfigurationComponent,
    children: [
      {
        path: "race",
        component: RaceComponent
      },
      {
        path: "class",
        component: ClassComponent
      },
      {
        path: "room",
        component: RoomComponent
      },
      {
        path: "NPC",
        component: NPCComponent
      },
      {
        path: "startequipment",
        component: StartequipmentComponent
      },
      {
        path: "command",
        component: CommandComponent
      },
      {
        path: "item",
        component: ItemComponent
      }
    ]
    // pathMatch: "full"
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigurationRoutingModule {
}

export const routingComponents = [ConfigurationComponent, RaceComponent];
