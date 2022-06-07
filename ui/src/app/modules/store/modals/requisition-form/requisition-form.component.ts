import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Dropdown } from "src/app/shared/modules/form/models/dropdown.model";
import { Field } from "src/app/shared/modules/form/models/field.model";
import { FormValue } from "src/app/shared/modules/form/models/form-value.model";
import { Textbox } from "src/app/shared/modules/form/models/text-box.model";
import { RequisitionInput } from "src/app/shared/resources/store/models/requisition-input.model";

@Component({
  selector: "app-requisition-form",
  templateUrl: "./requisition-form.component.html",
  styleUrls: ["./requisition-form.component.scss"],
})
export class RequisitionFormComponent implements OnInit {
  requisitionFields: Field<string>[];
  requisitionFormValue: FormValue;
  constructor(
    private dialogRef: MatDialogRef<RequisitionFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit() {
    this.requisitionFields = [
      new Dropdown({
        id: "requisition_item",
        key: "requisitionItem",
        label: "Item",
        required: true,
        options: [],
        shouldHaveLiveSearchForDropDownFields: true,
        searchControlType: "billableItem",
      }),
      new Dropdown({
        id: "target_store",
        key: "targetStore",
        label: "Target Store",
        required: true,
        options: (this.data?.stores || []).map((store) => ({
          key: store.id,
          value: store.id,
          label: store.name,
        })),
      }),
      new Textbox({
        id: "quantity",
        key: "quantity",
        label: "Quantity",
        required: true,
        type: "number",
      }),
    ];
  }

  onCancel(e: Event): void {
    e.stopPropagation();
    this.dialogRef.close();
  }

  onRequest(e: Event) {
    e.stopPropagation();
    const formValues = this.requisitionFormValue.getValues();
    const requisitionInput: RequisitionInput = {
      requestingLocationUuid: this.data?.currentStore?.id,
      requestedLocationUuid: formValues?.targetStore?.value,
      items: [
        {
          itemUuid: formValues?.requisitionItem?.value,
          quantity: parseInt(formValues?.quantity.value, 10),
        },
      ],
    };

    this.dialogRef.close({ requisitionInput });
  }

  onUpdateForm(formValue: FormValue): void {
    this.requisitionFormValue = formValue;
  }
}