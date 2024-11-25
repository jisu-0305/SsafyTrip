export const getInitials = (email) => {
  if (!email) return '';
  return email.charAt(0).toUpperCase();
};

export const getProfileColor = (email) => {
  if (!email) return 'grey';
  
  // 이메일의 해시값을 기반으로 색상 생성
  const colors = [
    'green',
    'secondary',
    'success',
    'info',
    'warning',
    'error',
    'purple',
    'indigo'
  ];
  
  const hash = email.split('').reduce((acc, char) => {
    return char.charCodeAt(0) + ((acc << 5) - acc);
  }, 0);
  
  return colors[Math.abs(hash) % colors.length];
}; 