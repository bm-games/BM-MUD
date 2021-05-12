import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {FaqComponent} from "./components/faq/faq.component";

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent
  },
  {
    path: "faq",
    component: FaqComponent
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
