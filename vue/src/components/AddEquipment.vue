<template>
    <div class="equipment">
    <div class="title">
        <h3>CREATE EQUIPMENT</h3>
      </div>
    <div class="input">
      <form @submit.prevent="submitForm">
        <div class="section">
          <label id="equipment-name">Equipment Name</label>
          <input v-model="newEquipment.equipmentName" required />
        </div> 
          <button type="submit">Submit</button>
      </form>
    </div>
  </div>
</template>

<script>
import EquipmentService from "../services/EquipmentService";
export default {
  name: "AddEquipment",
  data() {
    return {
      newEquipment: {
        equipmentName: ""
      }
    }
  },
  methods: {
    submitForm() {
      EquipmentService.addEquipment(this.newEquipment).then(() => {
        this.newEquipment = {
          equipmentName: ""
        }
      });
    }
  }

}
</script>

<style>

</style>