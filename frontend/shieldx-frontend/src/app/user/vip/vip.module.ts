import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { VipListComponent } from './vip-list/vip-list.component';
import { VipFormComponent } from './vip-form/vip-form.component';

@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild([
      { path: '', component: VipListComponent },
      { path: 'new', component: VipFormComponent },
      { path: 'self', component: VipFormComponent, data: { isSelf: true } },
      { path: 'edit/:id', component: VipFormComponent }
    ])
  ]
})
export class VipModule { }
