import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {DashboardRoutingModule} from "./dashboard-routing.module";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatOptionModule} from "@angular/material/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {GameOverviewComponent} from './components/game-overview/game-overview.component';
import {MatCardModule} from "@angular/material/card";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatChipsModule} from "@angular/material/chips";
import {MatDialogModule} from '@angular/material/dialog';
import {ChangePasswordDialog} from "./components/password/change-password-dialog";
import {DeleteDialog} from "./components/game-overview/delete-dialog.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {FaqComponent} from './components/faq/faq.component';
import {MatExpansionModule} from "@angular/material/expansion";
import {DraftsComponent} from './components/draft/drafts.component';
import {MatTabsModule} from "@angular/material/tabs";
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";
import {MatListModule} from "@angular/material/list";


@NgModule({
  declarations: [
    DashboardComponent,
    GameOverviewComponent,
    ChangePasswordDialog,
    DeleteDialog,
    FaqComponent,
    DraftsComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    ReactiveFormsModule,
    FormsModule,

    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatAutocompleteModule,
    MatCardModule,
    MatOptionModule,
    MatGridListModule,
    MatChipsModule,
    MatDialogModule,
    MatTooltipModule,
    MatExpansionModule,
    MatTabsModule,
    MatBottomSheetModule,
    MatListModule
  ]
})
export class DashboardModule {
}
