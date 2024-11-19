import myaxios from "@/util/axios.js";

function register(member) {
  myaxios.post("/register", JSON.stringify(customer));
}
export { register };
