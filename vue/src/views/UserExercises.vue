<template>
  <div>
    <UserCreateExercise :userId="parsedUserId" @exercise-created="refreshExercises"/>
  </div>
</template>

<script>
import UserCreateExercise from '../components/UserCreateExercise.vue';

export default {
  name: "UserExercises",
  components: {
    UserCreateExercise
  },
  props: {
    userId: {
      type: [Number, String], // Allow both Number and String types
      required: true,
    }
  },
  computed: {
    parsedUserId() {
      return parseInt(this.userId);
    }
  },
  methods: {
    refreshExercises() {
      this.$refs.UserExercise.refreshExercises();
    }
  }
};
</script>