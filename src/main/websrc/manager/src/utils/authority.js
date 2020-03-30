export function getAuthority() {
  return localStorage.getItem('manager_authority');
}

export function setAuthority(authority) {
  return localStorage.setItem('manager_authority', authority);
}
