import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { HomepageComponent } from './homepage/homepage.component';
import { ViewDetailComponent } from './view-detail/view-detail.component';
import { ViewInfoUserComponent } from './view-info-user/view-info-user.component';
import { ViewCartComponent } from './view-cart/view-cart.component';
import { ViewSearchComponent } from './view-search/view-search.component';


@NgModule({
  declarations: [HomepageComponent, ViewDetailComponent, ViewInfoUserComponent, ViewCartComponent, ViewSearchComponent],
  imports: [
    CommonModule,
    UserRoutingModule
  ]
})
export class UserModule { }
