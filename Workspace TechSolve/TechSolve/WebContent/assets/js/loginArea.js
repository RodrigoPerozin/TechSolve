function verifyLoginData(){
	
	var pass64 = btoa(document.frmLogin.senha.value);
	document.frmLogin.senha.value = pass64;
	
	return true;
}