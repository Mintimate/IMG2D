import { ToastProgrammatic as Toast } from 'buefy'

export function successTips(message){
  Toast.open({
    message:message,
    type:"is-success",
    duration:3500,
    pauseOnHover: true
  })
}

export function failTips(message){
  Toast.open({
    message:message,
    type:"is-danger",
    duration:3500,
    pauseOnHover: true
  })
}

export function warningTips(message){
  Toast.open({
    message:message,
    type:"is-warning",
    duration:3500,
    pauseOnHover: true
  })
}
